"use strict";

const express = require("express");
const api = express.Router();
const categoryController = require("./category.controller");
const { ensureAuth, isAdmin } = require("../services/authenticated");


api.get("/test", categoryController.test);

api.post("/add", [ensureAuth,isAdmin],categoryController.addCategory);
api.get("/get", categoryController.getCategories);
api.get("/get/:id", categoryController.getCategory);
api.put("/update/:id",[ensureAuth,isAdmin], categoryController.updateCategory);
api.delete("/delete/:id", [ensureAuth,isAdmin],categoryController.deleteCategory);

module.exports = api;
