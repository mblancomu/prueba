//En la clase que sea (Fragment o Clase sin más...tipo Presenter)

   public void onRequestPermissionsResult() {
        share(getContext(), title);
    }

    public void share(Context context, Title title) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionsHelper.requestPermission((Activity) context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                Utils.shareTitle(context, title);
        } else {
            Utils.shareTitle(context, title);
        }
    }



//En la Actividad registramos el resultado:

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsHelper.REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    playerFragment.onRequestPermissionsResult();
                } else {
                    playerFragment.onRequestPermissionsResult();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

