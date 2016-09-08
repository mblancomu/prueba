private void putMarkerType(HashMap<Marker, Trip> mMarkersHashMap){
	
	 LatLng position = getPositionFronDB(trip.getCoords());
            List<String> types = StringsUtils.convertStringToArrayList(trip.getType());

            for (int i = 0; i < types.size(); i++) {

                Marker markerTrip = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .icon(getIconByType(types.get(i).toString()))
                        .title(trip.getTitle())
                        .snippet(trip.getDescription()));

                mMarkersHashMap.put(markerTrip, trip);
                mMap.setInfoWindowAdapter(new CustomMarkerInfo(mContext, mMarkersHashMap));
            }
}