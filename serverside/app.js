var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');

Animals = require('./models/animals');

//Connect to mogoose

mongoose.connect('mongodb://localhost/cm');
var db = mongoose.connection;

app.get('/',function (req, res) {
    res.send('Hello World!')
});

app.get('/api/animals',function (req, res) {
    Animals.getAnimals(function (err, animals) {
        if (err) {
            throw err;
        }
        res.json(animals);
    })
});

app.get('/api/animals/:_id', function (req, res) {
    Animals.getAnimalByID(req.params._id, function (err, animals) {
      if (err) {
            throw err;
        }
        res.json(animals);
    })
});

app.post('api/addAnimal', function(req, res) {
    
})

/*app.post('/api/genres', (req, res) => {
	var genre = req.body;
	Genre.addGenre(genre, (err, genre) => {
		if(err){
			throw err;
		}
		res.json(genre);
	});
});*/

app.get('/api/animals/:_id')

app.listen(3000);
console.log("Listening on port 3000");
