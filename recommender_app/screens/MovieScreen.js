import React from "react";
import {
  StyleSheet,
  View,
  Button,
  Text,
  KeyboardAvoidingView
} from "react-native";
import MovieInput from "../components/MovieInputs";
import { ScrollView } from "react-native-gesture-handler";
import RNWinterMovies from "react-native-winter-movies";

export default class MovieScreen extends React.Component {
  static navigationOptions = {
    title: "Linked Data Movie Recommender",
    headerStyle: {
      backgroundColor: "#37d"
    },
    headerTitleStyle: {
      fontWeight: "bold"
    },
    headerTintColor: "#fff"
  };

  render() {
    return (
      <ScrollView style={styles.background}>
        <KeyboardAvoidingView
          enabled
        >

          <Text style={styles.infoText}>
            Get movie recommendations based on movies you have seen! Enter the
            information below and hit the button once you're finished.
          </Text>
          {[1, 2, 3, 4, 5].map(x => (
            <View key={"input" + x} style={styles.inputWrap}>
              <MovieInput
                placeholder={"Movie " + x}
              //style={styles.input}
              //underlineColorAndroid="transparent"
              />
            </View>
          ))}
          <View style={styles.inputWrap}>
            <MovieInput placeholder={"Genre"} />
            <MovieInput placeholder={"Year"} />
          </View>
          <View style={styles.buttonWrap}>
            <Button title="Run!" onPress={this._onRun} color="#37d" />
          </View>
        </KeyboardAvoidingView>
      </ScrollView>

    );
  }

  _onRun = () => {
    this.props.navigation.navigate("Result");
  };
}

const styles = StyleSheet.create({
  background: {
    flex: 1,
    paddingHorizontal: 20,
    backgroundColor: "#fff"
  },
  infoText: {
    fontFamily: "monospace",
    fontSize: 18,
    color: "#555",
    paddingVertical: 10
  },
  inputWrap: {
    flexDirection: "row",
    marginVertical: 4,
    height: 42,
    backgroundColor: "transparent"
  },
  buttonWrap: {
    //paddingVertical: 15,
    marginTop: 20
  }
});
