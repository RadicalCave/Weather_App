package com.example.weatherapp.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.model.Forecast

@Composable
fun ComposeRecyclerView(forecast: Forecast){
    Card (
        modifier = Modifier
            .width(110.dp)
            .height(120.dp)
            .padding(8.dp, 4.dp),
        RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp)
    ){
//        Surface {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Cyan)) {
//                Text(text = forecast.dt)
//                painterResource(id = )
//
//                Row(verticalAlignment = Alignment.Bottom,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.Yellow)) {
//                    Text(text = "Lowest")
//                    Text(text = "Highest")
//                }
//            }
//
//
//        }

    }
}


@Composable
fun ListItem(data: List<Forecast>){
    LazyRow {
        itemsIndexed(items = data){ index, item ->
            ComposeRecyclerView(forecast = item)
        }
    }
}

@Preview
@Composable
fun ComposablePreview(){
    ComposeRecyclerView(forecast = Forecast())
}