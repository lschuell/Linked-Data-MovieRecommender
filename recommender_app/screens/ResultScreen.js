import React, { Component } from 'react'
import { View, Text, StyleSheet } from 'react-native'
import createStackNavigator from 'react-navigation'


export default class ResultScreen extends Component {

	render() {
		return (
			<View style={styles.container}>
				<Text> Results </Text>
			</View>
		)
	}
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		backgroundColor: '#fff'
	}
})
