/**
 * @Author: Helder Ferreira
 * @Date:   2017-03-23T15:08:08+00:00
 * @Email:  helderferreira_@outlook.pt
 * @Last modified by:   Helder Ferreira
 * @Last modified time: 2017-04-16T17:53:56+01:00
 */



var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var Terraformer = require('terraformer');

//Notifications
var FCM = require('fcm-node');

var serverKey = require('./firebase/firebasePK.json'); //put the generated private key path here

var fcm = new FCM(serverKey)

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

app.get('/test',function (req, res) {
    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
            to: 'fy_kvHemwKE:APA91bGqZ7dtzYKJGBf26WqGGkf2q7mzEJBV_2mpknjIXR9N9q3aCfpa1w-RtIhYl2gRtroVH8RSDI2HiPG8BOM_ef8YKAdw7rHo5ke_cmGY1FrXtny7FaDnpWZzesSJJkfub0qtx2tA',

            notification: {
                title: 'Title of your push notification',
                body: 'Body of your push notification'
            }
        }

        fcm.send(message, function(err, response){
            if (err) {
                console.log("Something has gone wrong!")
            } else {
                console.log("Successfully sent with response: ", response)
            }
        })
});

function notifyUserAnimalOut(userReceived, animal) {
    // console.log("Entrei no notifyUserAnimalOut");
    console.log("User");
    console.log(userReceived);
    // console.log("Animal");
    // console.log(animal);

    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
        to: userReceived.fbToken,

        notification: {
            title: 'Animal excaped',
            body: animal.nome+' is outside of the fence!',
            sound: "default"
        }
    }

    console.log(message);

    fcm.send(message, function(err, response){
        if (err) {
            console.log("Something has gone wrong!")
        } else {
            console.log("Successfully sent with response: ", response)
        }
    })

}


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

    var object1 = {};

    object1.type = "Point";
    object1.coordinates = [parseFloat(location.longitude), parseFloat(location.latitude)];

    Animals.addAnimalLocation(idRecebido,{$push: {"location": location}}, function(err, animal) {
        if (err) {
            throw err;
        }

        User.getUserByID(animal.owner,function(err, user) {
            if (err) {
                throw err;
            }

            var newObject = {type: 'Polygon', coordinates: user.location[(user.location.length-1)].geometry}

            var polygon = new Terraformer.Primitive(newObject);
            var point = new Terraformer.Primitive(object1);

            if (point.within(polygon)) {
                console.log("Within of polygon");
            } else {
                console.log("Out of polygon");
            }

            notifyUserAnimalOut(user,animal);

        })
    });
    res.send({"status":"ok"});
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
    var fbToken = req.body.fbToken;


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
                updateFireBaseToken();
                res.json(result);
            } else {
                result = {
                    'result':'FAIL'
                };
                res.json(result);
            }

            function updateFireBaseToken() {
                User.updateUserByID(obj._id,{"fbToken": fbToken},function(err, animal) {
                    if (err) {
                        throw err;
                    }
                });
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
    console.log("addFence");
    id = req.body.idUser;
    coordenadas = req.body.coordenadas;

    const geometry = [coordenadas.map(result => [parseFloat(result.longitude), parseFloat(result.latitude)])]
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

    var animalToAdd = {};
    animalToAdd["id"] = idAnimal;

    User.addUserAnimals(idUser, {$push: {"animalsFollowing": animalToAdd}}, function(err, user) {
        if (err) {
            throw err;
        }
    });

    Animals.updateAnimal(idAnimal,{"owner": idUser},function(err,animal) {
        if (err) {
            throw err;
        }
    });

    res.json({"status":"ok"});
});

app.post('/api/getAnimalsFollowingLocation',function(req,res) {
    idUser = req.body.idUser;
    if (idUser==undefined) {
        res.json({'status' : 'noParameters'});
        return
    }
    console.log("getAnimalsFollowingLocation " + idUser);


    User.getUserByID(idUser,function(err,user) {
        if (err) {
            res.json({'status' : 'noUser'});
            return
        }
        var arrayOfLocation = [];
        var numberAnimals = user.animalsFollowing.length;

        if (numberAnimals==0) {
            res.json({'status' : 'noAnimals'});
            return
        }

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

app.post('/api/getAnimalsFollowingLocationAll',function(req,res) {
    idUser = req.body.idUser;
    if (idUser==undefined) {
        res.json({'status' : 'noParameters'});
        return
    }
    console.log("getAnimalsFollowingLocationAll " + idUser);


    User.getUserByID(idUser,function(err,user) {
        if (err) {
            res.json({'status' : 'noUser'});
            return
        }
        var arrayOfLocation = [];
        var numberAnimals = user.animalsFollowing.length;

        if (numberAnimals==0) {
            res.json({'status' : 'noAnimals'});
            return
        }

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
            animalObject["location"] = animal.location;
            arrayOfLocation.push(animalObject);
        }
    });
})

app.listen(3000);

console.log("Listening on port 3000");
