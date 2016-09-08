   /**
     * This method fetches the image from the phone
     */
    private void chooseImage() {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Starz" + File.separator);
        folder.mkdirs();
        String fName = "img_" + System.currentTimeMillis() + ".jpg";
        sdImageMainDirectory = new File(folder, fName);

        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        List<Intent> cameraIntents = new ArrayList<>();
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

        for (ResolveInfo resInfo : listCam) {
            String packageName = resInfo.activityInfo.packageName;
            Intent intent = new Intent(captureIntent);

            intent.setComponent(new ComponentName(resInfo.activityInfo.packageName, resInfo.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            cameraIntents.add(intent);
        }

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // TODO asset for "select action"?
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select action");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        //startActivityForResult(chooserIntent, SettingsActivity.REQUEST_CODE_IMG_SELECTED);

        activity.startActivityForResult(chooserIntent, SettingsActivity.REQUEST_CODE_IMG_SELECTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleAPIHelper.RC_READ || requestCode == GoogleAPIHelper.RC_SAVE) {
            presenter.onActivityResult(this, requestCode, resultCode, data);
            return;
        }

        boolean isCamera;
        isCamera = data == null || MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction());
        Uri selectedImageUri;
        if (isCamera) {
            selectedImageUri = outputFileUri;
        } else {
            selectedImageUri = data.getData();
        }
        try {
            Bitmap bitmap;
            if (isCamera)
                bitmap = rotateImage(sdImageMainDirectory);
            else
                bitmap = BitmapUtils.decodeSampledBitmapFromUri(activity.getContentResolver(), selectedImageUri, 512, 512);

            imageViewProfilePicture.setImageDrawable(new BitmapDrawable(activity.getResources(), bitmap));
            imageViewProfilePicture.setVisibility(View.VISIBLE);
            defImageViewProfilePicture.setVisibility(View.INVISIBLE);

            showProgress();
            presenter.updatePhotoUser(BitmapUtils.bitmapToEncodedString(bitmap), new SettingsPresenter.SettingsCallback() {
                @Override
                public void onSuccess(Object object) {
                    hideProgress();
                    displayInfo(null);
                }

                @Override
                public void onFailure(StarzPlayError error) {
                    hideProgress();
                    showError(error);
                }
            });


        } catch (NullPointerException e) {
            // TODO Set proper text or change way of handle this failure
            Crouton.makeText(activity, "Failed to get image", Style.ALERT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap rotateImage(File file) {
        Bitmap correctBmp = null;
        try {
            ExifInterface ei = new ExifInterface(file.getAbsolutePath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(file),
                    null, null);
            Bitmap rotateBm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), mat, true);

            int height = (512 * rotateBm.getHeight())
                    / rotateBm.getWidth();

            correctBmp = Bitmap.createScaledBitmap(rotateBm,
                    512, height, true);

        } catch (IOException e) {
            Log.w("TAG", "-- Error in setting image");
        } catch (OutOfMemoryError oom) {
            Log.w("TAG", "-- OOM Error in setting image");
        }

        return correctBmp;
    }