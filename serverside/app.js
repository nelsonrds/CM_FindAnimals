var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');

Animals = require('./models/animals');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

//Connect to mogoose

mongoose.connect('mongodb://localhost/cm');
var db = mongoose.connection;

app.get('/',function (req, res) {
    res.send('Hello World!')
});

app.get('/test',function (req, res) {
    result = {
        'result':'OK'
    };
    res.json(result);
})

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

app.post('/api/addAnimal', function(req, res) {
    var animal = req.body;
    Animals.addAnimal(animal, function(err, animal) {
        if (err) {
            throw err;
        }
        res.json(animal);
    })
});

app.put('/api/updateAnimalLocation/:_id', function(req, res) {
    var idRecebido = req.params._id;
    var location = req.body;
    location["_id"] = 'ObjectId()';
    location['treated'] = 'false';
    console.log(location);
    console.log("Entrei");


    Animals.addAnimalLocation(idRecebido,{$push: {"location": location}}, function(err, animal) {
        if (err) {
            throw err;
        }
        res.json(animal);
    })
});


app.listen(3000);

console.log("Listening on port 3000");
