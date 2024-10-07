# At Lunch

Find restaurants nearby your current location or by searching.

The app is manually interacting with the new Google Places API using Retrofit/OkHttp - this is done intentionally as the
take home assignment requirements specifically state not to use the Google's Places API client.

## Maps Access Token

The project uses the Gradle Secrets plugin to protect the Maps SDK Key. Therefore, when first checking out this
repository, make sure you add an access token within the `secrets.properties` file. This token is used both for the Maps
SDK and the Places (New) API - the key will need to have both APIs enabled from the projects Google Console.

## TODO

- [ ] Connect Search field UI to utilize Places Text API search found
  in [app/src/main/java/com/alltrails/atlunch/data/PlacesRepository.kt].
- [ ] Implement [Place Photo API](https://developers.google.com/maps/documentation/places/android-sdk/place-photos) and
  load restaurant images using Coil.
- [ ] Implement ability to bookmark favorite restaurants.
