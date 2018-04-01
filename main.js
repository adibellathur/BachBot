const electron = require('electron');
const url = require('url');
const path = require('path');

const {app, BrowserWindow} = electron;

let mainWindow;

//Step 1: Listen for the App to be ready

app.on('ready', function() {
	//Create HTML file
	mainWindow = new BrowserWindow({width: 900, height: 600});
	//Load HTML
	mainWindow.loadURL(url.format({
		pathname: path.join(__dirname, 'createWindow.html'),
		protocol: 'file:',
		slashes: true
	}));
});

