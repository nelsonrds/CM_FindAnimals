/**
 * @Author: Helder Ferreira
 * @Date:   2017-04-09T17:50:44+01:00
 * @Email:  helderferreira_@outlook.pt
 * @Last modified by:   Helder Ferreira
 * @Last modified time: 2017-04-09T18:26:57+01:00
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
    date: {
        type: Date,
        default: Date.now
    }
});

var User = module.exports = mongoose.model('User',usersSchema);

module.exports.addUser = function (user, callback) {
    User.create(user,callback);
}
