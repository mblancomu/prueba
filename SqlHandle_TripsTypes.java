 public static void populateTripsTypesDB(){

        try {

            String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    List<String> types = new ArrayList<>();
                    types = StringsUtils.convertStringToArrayList(cursor.getString(6));

                    for (int i=0;i < types.size();i++){
                        TripsTypesItem tripsTypes = new TripsTypesItem();
                        tripsTypes.setIdTrip(cursor.getString(1));
                        tripsTypes.setType(types.get(i).toString());

                        putTripsTypes(tripsTypes);

                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static List<String> getTypesByTripId(String idTrip){
        List<String> types = new ArrayList<>();
        try {

            String selectQuery = "SELECT  * FROM " + TripsTypesDB.TABLE_TRIPSTYPES  + " where id_trip='" + idTrip + "'";

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    
                    types.add(cursor.getString(2));

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return types;
    }

    public static void putTripsTypes(TripsTypesItem tripsTypes){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripsTypesDB.COLUMN_IDTRIP, tripsTypes.getIdTrip());
        values.put(TripsTypesDB.COLUMN_TYPE, tripsTypes.getType());
        db.insert(TripsTypesDB.TABLE_TRIPSTYPES, null, values);
    }

    public static void putUserState(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDB.COLUMN_USER, user.getUsername()); 
        values.put(UserDB.COLUMN_STATE, user.getState();
        db.insert(UserDB.TABLE_USER, null, values);
    }

    public static User getUserState(){
        User user = new User();

        try {

            String selectQuery = "SELECT  * FROM " + UserDB.TABLE_USER;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    
                    user.setUsername(cursor.getString(1));
                    user.setState(cursor.getString(2));

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return user;
    }