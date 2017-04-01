var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');

Animals = require('./models/animals');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));



app.use(function (req, res, next) {

    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', '*');

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');

    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);

    // Pass to next layer of middleware
    next();
    console.log("Entrei no Access-Control-Allow-Credentials");
});

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
        res.json({animals});
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

app.get('/api/animalExists/:_id', function (req, res) {
    result = {'exists':'true'};
    Animals.getAnimalByID(req.params._id, function(err, animals) {
        console.log("id "+ req.params._id);
        console.log("erro "+ err);
        //console.log("callback "+animals.length);
        if (err) {
            result = {'exists':'false'};
        } else {
            result = {'exists':'true'};
        }
        res.json(result);
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
    location["_id"] = mongoose.Types.ObjectId();
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
