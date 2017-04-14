/**
 * @Author: Helder Ferreira
 * @Date:   2017-03-23T15:08:08+00:00
 * @Email:  helderferreira_@outlook.pt
 * @Last modified by:   Helder Ferreira
 * @Last modified time: 2017-04-14T15:00:50+01:00
 */



var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var GeoJSON = require('geojson');

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

app.get('/api/getFence/:_id', function (req, res) {
    idRecebido = req.params._id;
    User.getUserByID(idRecebido,function(err, user) {
        var resultadoChamada = {};

        if (err) {
            resultadoChamada["status"] = "idNotFound";
        } else {
            resultadoChamada["status"] = "clear";
        }

        data = user.locationInCoordinates[(user.locationInCoordinates.length-1)];
        if (data!=undefined) {
            resultadoChamada["status"] = "ok";
            resultadoChamada["coordenadas"] = data;
        }

        res.json(resultadoChamada);

    });
});

app.post('/api/addAnimalToUser',function(req,res) {
    idUser = req.body.idUser;
    idAnimal = req.body.idAnimal;
    console.log(idUser);
    console.log(idAnimal);

    var animalToAdd = {};
    animalToAdd["id"] = idAnimal;

    User.addUserAnimals(idUser, {$push: {"animalsFollowing": animalToAdd}}, function(err, user) {
        if (err) {
            throw err;
        }
    });

    res.json({"status":"ok"});
});

app.post('/api/getAnimalsFollowingLocation',function(req,res) {
    idUser = req.body.idUser;
    console.log(idUser);


    User.getUserByID(idUser,function(err,user) {
        if (err) {
            throw err;
        }
        var arrayOfLocation = [];
        var numberAnimals = user.animalsFollowing.length;

        console.log("numberAnimals " + numberAnimals);

        user.animalsFollowing.forEach(function(animal) {
            Animals.getAnimalByID(animal.id, function (err, animals) {
                if (err) {
                    throw err;
                }
                addLocationArray(animals);
                deCount();
            })
        });
        function deCount() {
            numberAnimals--;
            if (numberAnimals<1) {
                console.log(arrayOfLocation);
                var finalObject = {};
                finalObject["status"] = "ok";
                finalObject["animals"] = arrayOfLocation;
                res.json(finalObject);
            }
        }

        function addLocationArray (animal) {
            var animalObject = {};

            animalObject["id"] = animal.id;
            animalObject["nome"] = animal.nome;
            animalObject["lastLocation"] = animal.location[(animal.location.length-1)];
            arrayOfLocation.push(animalObject);
        }

    });
})

app.listen(3000);

console.log("Listening on port 3000");
