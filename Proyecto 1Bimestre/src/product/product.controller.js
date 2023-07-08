"use strict";

const Product = require("./product.model");
const User = require("../user/user.model");
const Category = require("../category/category.model");
const { deleteSensitiveData } = require("../utils/validate");

exports.test = (req, res) => {
  res.send({ message: "Test function is running" });
};

exports.getProducts = async (req, res) => {
  try {
    let products = await Product.find();
    return res.send({ products });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error getting products" });
  }
};

exports.findbyname = async (req, res) => {
  try {
    let productname = req.body.name;
    let products = await Product.find({ name: productname });
    return res.send(products);
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Invalid name" });
  }
};
exports.findbycategory = async (req, res) => {
  try {
    let data=req.body;
    let category = await Category.findOne({ name: data.category });
    let userId = category._id;
    let products = await Product.find({ category: userId });
    return res.send(products);
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Invalid name" });
  }
};

exports.save = async (req, res) => {
  try {
    let data = req.body;
    let dataRequired = data.category;
    let existProduct = await Product.findOne({ name: data.name });
    
    if (existProduct) {
      return res.send({ message: "Product already created" });
    }
    
    if (!dataRequired)
      return res.status(400).send({ message: "Param category is required" });
    let alreadyCategory = await Category.findOne({ _id: data.category });
    if (!alreadyCategory)
      return res.status(400).send({ message: "Category not found" });
    let user = await User.findOne({ _id: data.user });
    if (!user || user.role != "ADMIN")
      return res
        .status(404)
        .send({ message: "Product not found or you dont have permission" });
    let product = new Product(data);
    await product.save();
    return res.send({ message: "Product created succesfully" });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error adding product", err });
  }
};

exports.getProduct = async (req, res) => {
  try {
    let productId = req.params.id;
    let product = await Product.findOne({ _id: productId })
    if (!product) return res.status(404).send({ message: "Product not found" });
    return res.send({ product });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error getting product" });
  }
};
exports.delete = async (req, res) => {
  try {
    let productId = req.params.id;
    let productDeleted = await Product.deleteOne({ _id: productId });
    if (!productDeleted.deletedCount === 0)
      return res
        .status(404)
        .send({ message: "Product not found and not deleted" });
    return res.send({ message: "Product deleted" });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error deleting product" });
  }
};

exports.updateProduct = async (req, res) => {
  try {
    let productId = req.params.id;
    let data = req.body;
    let existProduct = await Product.findOne({ name: data.name });
    if (existProduct) {
      if (existProduct._id != productId)
        return res.send({ message: "Product already created" });
      let updatedProduct = await Product.findOneAndUpdate(
        { _id: productId },
        data,
        {
          new: true,
        }
      );
      if (!updatedProduct)
        return res
          .status(404)
          .send({ message: "Product not found and not updated" });
      return res.send({ updatedProduct });
    }
    let updatedProduct = await Product.findOneAndUpdate(
      { _id: productId },
      data,
      {
        new: true,
      }
    );
    if (!updatedProduct)
      return res
        .status(404)
        .send({ message: "Product not found and not updated" });
    return res.send({ updatedProduct });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error updating Product" });
  }
};
exports.topSale = async(req,res)=>{
  try {
      let product = await Product.find({},{category:0}).sort({sales:-1});
          return res.send({message:'Top Sale',product});
  } catch (err) {
      console.error(err);
      return res.status(500).send({message:'Error searching'});
  }
}
exports.soldOut= async(req,res)=>{
  try{
    let quantity = 0;
    let products = await Product.find({ stock: quantity });
    return res.send(products);
  }catch(err){
    console.error(err)
    return res.status(500).send({message:"Error searching"});
  }
}