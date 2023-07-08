"use strict";

const userController = require("./user.controller");
const express = require("express");
const api = express.Router();
const { ensureAuth, isAdmin } = require("../services/authenticated");

api.get("/", ensureAuth, userController.test);
api.post("/register", userController.register);
api.get("/get", userController.getUsers);
api.post("/login", userController.login);
api.put("/updateUser", ensureAuth, userController.updateMyUser);
api.put("/update/:id", [ensureAuth,isAdmin], userController.updateUser);
api.delete("/deleteUser/:id", [ensureAuth,isAdmin],userController.deleteUser);
api.delete("/delete", ensureAuth, userController.deleteProfile);

module.exports = api;
