"use strict";

const jwt = require("jsonwebtoken");

exports.createToken = (user) => {
  let payload = {
    sub: user._id,
    name: user.name,
    surname: user.surname,
    username: user.username,
    email: user.email,
    role: user.role,
    iat: Date.now(),
    exp: Date.now() + 60 * 60,
  };
  let token = jwt.sign(payload, `${process.env.SECRET_KEY}`);
  return token;
};
