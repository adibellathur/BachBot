const electron = require('electron');
const url = require('url');
const path = require('path');
const Store = require('electron-store');

const {app, BrowserWindow} = electron;
const store = new Store();

let mainWindow;

//Step 1: Listen for the App to be ready

app.on('ready', function() {
	//Create HTML file
	mainWindow = new BrowserWindow({
		width: 1100,
		height: 700,
		backgroundColor: '#000000',
		show: false
	}); //titleBarStyle: 'hidden'
	//Load HTML
	mainWindow.loadURL(url.format({
		pathname: path.join(__dirname, 'html/homepage.html'),
		protocol: 'file:',
		slashes: true
	}));

	mainWindow.once('ready-to-show', () => {
		mainWindow.show()
	});

	console.log(app.getPath('userData'));

});
