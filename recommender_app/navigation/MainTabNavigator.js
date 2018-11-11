import React from "react";
import { Platform } from "react-native";
import {
  createStackNavigator,
  createBottomTabNavigator
} from "react-navigation";

import TabBarIcon from "../components/TabBarIcon";
import MovieScreen from "../screens/MovieScreen";
import BookScreen from "../screens/BookScreen";
import SettingsScreen from "../screens/SettingsScreen";
import ResultScreen from "../screens/ResultScreen";

const MovieStack = createStackNavigator({
  Movie: MovieScreen,
  Result: ResultScreen
});

MovieStack.navigationOptions = {
  tabBarLabel: "Movies",

};

const BookStack = createStackNavigator({
  Book: BookScreen
});

BookStack.navigationOptions = {
  tabBarLabel: "Books",

};

const SettingsStack = createStackNavigator({
  Settings: SettingsScreen
});

SettingsStack.navigationOptions = {
  tabBarLabel: "Settings",

};

export default createBottomTabNavigator(
  {
    Movie: MovieStack,
    Books: BookStack,
    Settings: SettingsStack
  },
  {
    tabBarOptions: {
      //activeTintColor: "#6b3"
    }
  }
);
