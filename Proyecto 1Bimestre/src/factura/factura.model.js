"use strict";

const mongoose = require("mongoose");

const productSchema = mongoose.Schema(
  {
    fecha: {
      type: Date,
      required: true,
    },
    products: {
      type: [mongoose.Schema.Types.ObjectId, mongoose.Schema.Types.Array],
      ref: "Product",
    },
    total: {
      type: Number,
      required: true,
    },
    cliente: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
      required: false,
    },
  },
  {
    versionKey: false,
  }
);

module.exports = mongoose.model("Factura", productSchema);
