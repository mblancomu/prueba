    public void onRequestPermissionsResult() {
        changePhoto(getContext());
    }

    public void changePhoto(Context context) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionsHelper.requestPermission((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                chooseImage();
        } else {
            chooseImage();
        }
    }

    if (imageViewProfilePicture != null) {
            if (photoUrl != null && !StringUtils.isEmpty(photoUrl)) {

               /* Picasso.with(activity)
                        .load(photoUrl)
                        .centerCrop()
                        .resize(activity.getResources().getDimensionPixelOffset(R.dimen.profile_pic_big), activity.getResources().getDimensionPixelOffset(R.dimen.profile_pic_big))
                        .error(R.drawable.no_content_error_03)
                        .into(imageViewProfilePicture);*/

                Glide.with(activity)
                        .load(photoUrl)
                        .asBitmap()
                        .centerCrop()
                        .override(activity.getResources().getDimensionPixelOffset(R.dimen.profile_pic_big), activity.getResources().getDimensionPixelOffset(R.dimen.profile_pic_big))
                        .error(R.drawable.no_content_error_03)
                        .into(new BitmapImageViewTarget(imageViewProfilePicture) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageViewProfilePicture.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                imageViewProfilePicture.setVisibility(View.VISIBLE);
                defImageViewProfilePicture.setVisibility(View.INVISIBLE);
            } else {
                imageViewProfilePicture.setVisibility(View.INVISIBLE);
                defImageViewProfilePicture.setVisibility(View.VISIBLE);
            }
        }