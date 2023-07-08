"use strict";

const express = require("express");
const api = express.Router();
const productController = require("./product.controller");
const { ensureAuth, isAdmin } = require("../services/authenticated");

api.get("/test", productController.test);
api.post("/save", [ensureAuth, isAdmin], productController.save);
api.get("/get", [ensureAuth], productController.getProducts);
api.get("/get/:id", ensureAuth, productController.getProduct);
api.delete("/delete/:id", [ensureAuth, isAdmin], productController.delete);
api.post("/findbyname", productController.findbyname);
api.post("/find", productController.findbycategory);
api.put("/updateProduct/:id",[ensureAuth, isAdmin],productController.updateProduct);
api.get('/getSales',ensureAuth, productController.topSale)
api.post("/findbyname", productController.findbyname);
api.get("/soldOut",[ensureAuth,isAdmin],productController.soldOut);

module.exports = api;
