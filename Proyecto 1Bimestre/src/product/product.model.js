"use strict";

const mongoose = require("mongoose");

const productSchema = mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
      unique: true,
    },
    stock: {
      type: Number,
      required: true,
    },
    price: {
      type: Number,
      required: true,
    },
    category: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Category",
      required: false,
    },
    sales: {
      type: Number,
      default: 0,
    },
  },
  {
    versionKey: false,
  }
);

module.exports = mongoose.model("Product", productSchema);
