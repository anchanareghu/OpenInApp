package com.example.myopeninapp.ui.components.navigation

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myopeninapp.R
import com.example.myopeninapp.com.example.myopeninapp.ui.components.campaigns.CampaignScreen
import com.example.myopeninapp.com.example.myopeninapp.ui.components.courses.CoursesScreen
import com.example.myopeninapp.com.example.myopeninapp.ui.components.links.DashboardScreen
import com.example.myopeninapp.ui.components.profile.ProfileScreen
import com.example.myopeninapp.ui.theme.Blue
import com.example.myopeninapp.ui.theme.MyOpenInAppTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationItem(
            title = "Links",
            icon = R.drawable.links,
            route = "links"
        ),
        BottomNavigationItem(
            title = "Courses",
            icon = R.drawable.courses,
            route = "courses"
        ),
        BottomNavigationItem(
            title = "",
            icon = R.drawable.blank,
            route = ""
        ),
        BottomNavigationItem(
            title = "Campaigns",
            icon = R.drawable.campaigns,
            route = "campaigns"
        ),
        BottomNavigationItem(
            title = "Profile",
            icon = R.drawable.profile,
            route = "profile"
        )
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        },
                        label = { Text(text = item.title, fontSize = 9.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Gray
                        ),
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = Blue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.offset(y = (58).dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }, floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = "links"
        ) {
            composable("links") {
                DashboardScreen()
            }
            composable("courses") {
                CoursesScreen()
            }
            composable("campaigns") {
                CampaignScreen()
            }
            composable("profile") {
                ProfileScreen()
            }
        }
    }
}


data class BottomNavigationItem(
    val title: String,
    val icon: Int,
    val route: String
)

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyOpenInAppTheme {
        MainScreen()
    }
}
