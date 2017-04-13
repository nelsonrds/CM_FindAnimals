/**
 * @Author: Helder Ferreira
 * @Date:   2017-04-09T17:50:44+01:00
 * @Email:  helderferreira_@outlook.pt
 * @Last modified by:   Helder Ferreira
 * @Last modified time: 2017-04-13T20:20:40+01:00
 */

var mongoose = require('mongoose');

// User Schema

var usersSchema = mongoose.Schema({
    user: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true,
    },
    name: {
        type: String,
        required: true
    },
    address: {
        type: String,
        required: true,
    },
    email: {
        type: String,
        required: true,
    },
    date: {
        type: Date,
        default: Date.now
    },
    location: [{}],
    locationInCoordinates: [{}]
});

var User = module.exports = mongoose.model('User',usersSchema);

module.exports.addUser = function (user, callback) {
    User.create(user,callback);
}

module.exports.loginCheck = function (user, pass, callback) {
    User.findOne({user:user}, function(error, object) {
        if (object == null) {
            callback(null);
        } else {
            if (object.password==pass) {
                callback(object);
            } else {
                callback(null);
            }
        }
    });
}

module.exports.getUsers = function (callback, limit) {
    User.find(callback).limit(limit);
};

module.exports.addUserFence = function (id,locals,callback) {
    User.findByIdAndUpdate(id, locals, {safe: true, upsert: true, new : true, strict: false},function(err, user) {
        callback(err,user);
    });
};

module.exports.getUserByID = function(id, callback) {
    User.findById(id,callback);
};
