/**
 * @Author: Helder Ferreira
 * @Date:   2017-03-23T15:08:08+00:00
 * @Email:  helderferreira_@outlook.pt
 * @Last modified by:   Helder Ferreira
 * @Last modified time: 2017-04-17T01:03:09+01:00
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
    },
    treated: {
        type: Boolean,
    },
    dateCreation: { type: Date, default: Date.now },
    location: [{}]

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

module.exports.addAnimal = function (animal, callback) {
    Animals.create(animal, callback);
};

module.exports.addAnimalLocation = function (id,locals, callback) {
    Animals.findByIdAndUpdate(id, locals, {safe: true, upsert: true, new : true, strict: false},function(err, animal) {
        callback(err,animal);
    });
};

module.exports.updateAnimal = function(id,cmd,callback) {
    Animals.findByIdAndUpdate(id,cmd,{},function(err,animal) {
        callback(err,animal);
    });
}
