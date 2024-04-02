package com.example.wat2eat.ui.prefs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wat2eat.R
import com.example.wat2eat.models.Preference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrefPopup(
    visible: Boolean,
    state: PrefsUiState,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    scope: CoroutineScope

) {
    var openBottomSheet by remember { mutableStateOf(visible) }

    LaunchedEffect(visible) {
        if(visible) {
            openBottomSheet = true
            scope.launch { sheetState.expand() }
        } else {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                openBottomSheet = false
            }
        }
    }


    if (openBottomSheet && sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                openBottomSheet = false
                onDismiss()
            },
            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp
            ),
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier.fillMaxHeight(),


            ) {
            LazyColumn {
                item {
                    Header()
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    PrefGrids(state)
                }
            }
        }
    }

}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.food_img),
            contentDescription = "User profile image",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text="Bon Appetit, user",
            color = Color.Black,
            style= TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W500,
                fontSize = 25.sp,

                ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text="Edit your dietary preferences:",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontSize = 15.sp
            )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PrefGrids(state: PrefsUiState) {
    val listState = rememberLazyStaggeredGridState()
    //for pref in state.activePrefs
    val items = (state.prefs.indices).map {
        Preference(
            name = state.prefs[it].name,
            category = state.prefs[it].category,
            active = state.prefs[it].active
        )
    }
    FlowRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ){
        repeat(state.prefs.size) {i->
            PrefItem(state.prefs[i], state)
        }
    }
}


@Composable
fun PrefItem(pref: Preference, state: PrefsUiState) {

    var selected by remember { mutableStateOf(pref.active) }
    val color = if(selected) BrightSage else Color.LightGray
    Button(
        modifier = Modifier
//            .width(20.dp)
            .height(40.dp)
            .padding(4.dp)
        ,
        onClick = {
            selected = !selected
            pref.active = !pref.active
            if(state.activePrefs.isNullOrEmpty()){
                state.activePrefs?.add(pref)
            }
            else if(!state.activePrefs.contains(pref)){
                state.activePrefs.add(pref)
            } else {
                state.activePrefs.remove(pref)
            }
        },
        colors =  if (selected) ButtonDefaults.buttonColors(BrightSage) else ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = pref.name, color = Color.Black, modifier = Modifier.align(Alignment.CenterVertically))
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//@Preview
//fun PreviewPrefs() {
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true
//    )
//    PrefPopup(true,
//        state = PrefsUiState(
//            activePrefs = samplePreferences.toMutableList(),
//        ),
//        sheetState = sheetState,
//        onDismiss = { sheetState.hide() }
//
//    )
//}

//@Composable
//@Preview
//fun PreviewPref() {
//    val pref = PreferenceObject(
//        name = "Vegetarian",
//        category = "Health",
//        active = true
//    )
//
//    PrefItem(pref)
//}

