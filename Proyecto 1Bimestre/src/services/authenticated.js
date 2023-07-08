"use strict";

const jwt = require("jsonwebtoken");

exports.ensureAuth = (req, res, next) => {
  if (!req.headers.authorization) {
    return res
      .status(403)
      .send({ message: `Doesnt contain headers "AUTHORIZATION"` });
  } else {
    try {
      let token = req.headers.authorization.replace(/[' "]+/g, "");
      var payload = jwt.decode(token, `${process.env.SECRET_KEY}`);
      if (payload.exp >= Date.now()) {
        return res.status(401).send({ message: "Expired Token" });
      }
    } catch (err) {
      console.error(err);
      return res.status(400).send({ message: "Invalid Token" });
    }
    req.user = payload;
    next();
  }
};

exports.isAdmin = async (req, res, next) => {
  try {
    let user = req.user;
    if (user.role !== "ADMIN")
      return res.status(403).send({ message: "Unauthorized User" });
    next();
  } catch (err) {
    console.error(err);
    return res.status(403).send({ message: "Unauthorized user" });
  }
};
