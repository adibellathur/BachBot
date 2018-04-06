const electron = require('electron');
const url = require('url');
const path = require('path');

const {app, BrowserWindow} = electron;

let mainWindow;

//Step 1: Listen for the App to be ready

app.on('ready', function() {
	//Create HTML file
	mainWindow = new BrowserWindow({width: 1100, height: 700}); //titleBarStyle: 'hidden'
	//Load HTML
	mainWindow.loadURL(url.format({
		pathname: path.join(__dirname, 'html/index.html'),
		protocol: 'file:',
		slashes: true
	}));
});
