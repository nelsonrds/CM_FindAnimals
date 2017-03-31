/**
 * Created by helder on 22/03/2017.
 */

var mongoose = require('mongoose');

// Animals Schema

var animalsSchema = mongoose.Schema({
    nome: {
        type: String,
        required: true
    },
    owner: {
        type: String,
        required: true
    },
    treated: {
        type: Boolean,
        required: true
    },
    dateCreation: { type: Date, default: Date.now }
    //location: [{longitude: Number, latitude: Number}],

});

var Animals = module.exports = mongoose.model('Animals',animalsSchema);

// Get Animals

module.exports.getAnimals = function (callback, limit) {
    Animals.find(callback).limit(limit);
};

//Get Animal by ID

module.exports.getAnimalByID = function (id, callback) {
    Animals.findById(id,callback);
};

//Add Animal

module.exports.addAnimal = (animal, callback) => {
	Animals.create(animal, callback);
}

module.exports.addAnimal = function (animal, callback) {
    Animals.create(animal, callback);
};

module.exports.addAnimalLocation = function (id,locals, callback) {
    Animals.findByIdAndUpdate(id, locals, {safe: true, upsert: true, new : true, strict: false},function(err, animal) {
        callback(err,animal);
    });
};