/**
 * @Author: Helder Ferreira
 * @Date:   2017-03-23T15:08:08+00:00
 * @Email:  helderferreira_@outlook.pt
 * @Last modified by:   Helder Ferreira
 * @Last modified time: 2017-04-13T16:11:05+01:00
 */



var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');

Animals = require('./models/animals');
User = require('./models/user');

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
});

//Connect to mogoose

mongoose.connect('mongodb://localhost/cm');
var db = mongoose.connection;

app.get('/',function (req, res) {
    res.send('Hello World!')
});


//#################################
//### Animals
//#################################

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


    Animals.addAnimalLocation(idRecebido,{$push: {"location": location}}, function(err, animal) {
        if (err) {
            throw err;
        }
        res.json(animal);
    })
});


//#################################
//### Users
//#################################

app.post('/api/addUser', function(req, res) {
    var user = req.body;
    User.addUser(user, function(err, user) {
        if (err) {
            throw err;
        }
        res.json(user);
    });
});

app.post('/api/loginCheck', function(req, res) {
    var user = req.body.user;
    var pass = req.body.password;

    if (user == null || pass == null) {

        result = {
            'result':'User or Password not Defined'
        };
        res.json(result);

    } else {

        User.loginCheck(user,pass,function(obj) {
            if (obj != null) {
                result = {
                    'result':'OK',
                    'user': obj
                };
                res.json(result);
            } else {
                result = {
                    'result':'FAIL'
                };
                res.json(result);
            }
        });
    }
});

app.get('/api/users',function (req, res) {
    User.getUsers(function (err, users) {
        if (err) {
            throw err;
        }
        res.json(users);
    })
});

app.post('/api/user/addFence', function(req, res) {
    console.log("\n");
    id = req.body.idUser;
    coordenadas = req.body.coordenadas;

    const geometry = [coordenadas.map(result => [result.latitude, result.longitude])]
    const finalObject = {
        type: 'Polygon',
        geometry
    }


    User.addUserFence(id,{$push: {"location": finalObject}}, function(err, user) {
        if (err) {
            throw err;
        }
    });

    User.addUserFence(id,{$push: {"locationInCoordinates": coordenadas}}, function(err, user) {
        if (err) {
            throw err;
        }
    });
    res.json({result:"true"});
});

function returnPares(coordenada) {

    var arr = {};
    arr.latitude = coordenada.latitude;
    arr.longitude = coordenada.longitude;
    console.log(JSON.stringify(arr));
    return arr;
}

app.listen(3000);

console.log("Listening on port 3000");
