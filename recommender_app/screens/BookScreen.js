import React from "react";
import { StyleSheet, View, Button, Text } from "react-native";
import MovieInput from "../components/MovieInputs";
import { ScrollView, TextInput } from "react-native-gesture-handler";

export default class MovieScreen extends React.Component {
  static navigationOptions = {
    title: "SWT Book Recommender",
    headerStyle: {
      backgroundColor: "#fff"
    },
    headerTitleStyle: {
      fontWeight: "bold"
    }
    //headerTintColor: "#fff"
  };

  render() {
    return (
      <ScrollView style={styles.background}>
        <Text style={styles.infoText}>
          Get book recommendations based on books you have read! Enter the
          information below and hit the button once you're finished.
        </Text>
        {[1, 2, 3, 4, 5].map(x => (
          <View key={"input" + x} style={styles.inputWrap}>
            <MovieInput placeholder={"Book " + x} />
          </View>
        ))}
        <View style={styles.inputWrap}>
          <MovieInput placeholder={"Genre"} />
          <MovieInput placeholder={"Year"} />
        </View>
        <View style={styles.buttonWrap}>
          <Button title="Run!" onPress={this._onRun} color="#36c" />
        </View>
      </ScrollView>
    );
  }

  _onRun = () => {};
}

const styles = StyleSheet.create({
  background: {
    //flex: 1,
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
    borderColor: "black",
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
