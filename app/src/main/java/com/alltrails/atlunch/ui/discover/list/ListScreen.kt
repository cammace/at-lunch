package com.alltrails.atlunch.ui.discover.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alltrails.atlunch.R
import com.alltrails.atlunch.data.model.Restaurant
import com.alltrails.atlunch.ui.theme.AtLunchTheme
import com.google.android.gms.maps.model.LatLng

@Composable
fun ListScreen(modifier: Modifier = Modifier, restaurants: List<Restaurant>) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(restaurants) {
            RestaurantCard(restaurant = it)
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // TODO implement loading image.
            CardMetaData(restaurant)
        }
    }
}

@Composable
private fun CardMetaData(restaurant: Restaurant) {
    Column {
        Text(text = restaurant.name, style = MaterialTheme.typography.titleSmall)
        Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(R.drawable.ic_star), tint = Color(0xFF5DE372), contentDescription = null)
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = restaurant.rating.toString(), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "•", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = pluralStringResource(
                    R.plurals.user_rating_count_format,
                    restaurant.userRatingCount,
                    restaurant.userRatingCount
                ),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF656E5E))
            )
        }
        Text(text = restaurant.address, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF656E5E)))
    }
}

@Preview
@Composable
private fun RestaurantCardPreview() {
    val restaurant = Restaurant(
        name = "Starbucks",
        rating = 4.5,
        address = "San Francisco",
        userRatingCount = 28,
        latLng = LatLng(37.7807705, -122.4339193)
    )

    AtLunchTheme {
        RestaurantCard(restaurant = restaurant)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ListScreenPreview() {
    val restaurants = listOf(
        Restaurant(
            name = "Starbucks",
            rating = 4.5,
            address = "San Francisco",
            userRatingCount = 28,
            latLng = LatLng(37.7807705, -122.4339193)
        ),
        Restaurant(
            name = "Taco Bell",
            rating = 2.3,
            address = "San Francisco",
            userRatingCount = 156,
            latLng = LatLng(37.7807705, -122.4339193)
        ),
        Restaurant(
            name = "Chick Fil A",
            rating = 4.9,
            address = "Mountain View",
            userRatingCount = 1,
            latLng = LatLng(37.7807705, -122.4339193)
        ),
    )

    AtLunchTheme {
        ListScreen(restaurants = restaurants)
    }
}
