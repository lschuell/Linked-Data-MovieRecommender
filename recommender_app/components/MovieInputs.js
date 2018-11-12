import React, { Component } from "react";
import { TextInput, View, StyleSheet } from "react-native";

class MovieInput extends Component {
  state = {
    text: ""
  };
  render() {
    return (
      <TextInput
        placeholder={this.props.placeholder}
        onChangeText={text => this.setState({ text })}
        value={this.state.text}
        style={styles.input}
        underlineColorAndroid="#ccc"
      />
    );
  }
}

export default MovieInput;

const styles = StyleSheet.create({
  input: {
    flex: 1,
    paddingHorizontal: 10,
    backgroundColor: "#fff",
    fontSize: 17
  }
});
