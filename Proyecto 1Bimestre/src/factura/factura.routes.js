"use strict";

const express = require("express");
const api = express.Router();
const facturaController = require("./factura.controller");
const { ensureAuth, isAdmin } = require("../services/authenticated");

api.post("/createdBill", ensureAuth, facturaController.factura);
api.post("/addToCart", ensureAuth, facturaController.addCart);
api.get( "/getproducts/:id",[ensureAuth, isAdmin],facturaController.getProducts);
api.get("/find/:id", ensureAuth, facturaController.findBills);
/*api.get('/getBillForUser/:id', [ensureAuth,isAdmin],billController.getBillForUser);
api.put('/update/deleteProductToExistBill/:id',[ensureAuth,isAdmin], billController.deleteProductToBill);
*/
module.exports = api;
