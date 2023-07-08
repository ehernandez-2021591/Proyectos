"use strict";

const Category = require("./category.model");
const Product = require("../product/product.model");

exports.test = (req, res) => {
  res.send({ message: "test category running" });
};

exports.defaultCategory = async () => {
  try {
    let defCategory = {
      name: "Default",
      description: "Category default",
    };
    let existCategory = await Category.findOne({ name: "Default" });
    if (existCategory) return console.log("Default category already created");
    let createdDefault = new Category(defCategory);
    await createdDefault.save();
    return console.log("Default category created");
  } catch (err) {
    return console.error(err);
  }
};

exports.addCategory = async (req, res) => {
  try {
    let data = req.body;
    let existCategory = await Category.findOne({ name: data.name });
    if (existCategory) {
      return res.send({ message: "Category already created" });
    }
    let newCategory = new Category(data);
    await newCategory.save();
    return res.status(201).send({ message: "Created category" });
  } catch (err) {
    console.log(err);
  }
};

exports.getCategories = async (req, res) => {
  try {
    let categories = await Category.find();
    return res.send({ categories });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error getting categories" });
  }
};

exports.getCategory = async (req, res) => {
  try {
    let categoryId = req.params.id;
    let category = await Category.findOne({ _id: categoryId });
    if (!category)
      return res.status(404).send({ message: "Category not found" });
    return res.send({ message: "Category found:", category });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error getting category" });
  }
};

exports.updateCategory = async (req, res) => {
  try {
    let categoryId = req.params.id;
    let data = req.body;
    let existCategory = await Category.findOne({ name: data.name });
    if (existCategory) {
      if (existCategory._id != categoryId)
        return res.send({ message: "Category already created" });
      let updatedCategory = await Category.findOneAndUpdate(
        { _id: categoryId },
        data,
        { new: true }
      );
      if (!updatedCategory)
        return res
          .status(404)
          .se0nd({ message: "Category not found and not updated" });
      return res.send({ updatedCategory });
    }
    let updatedCategory = await Category.findOneAndUpdate(
      { _id: categoryId },
      data,
      { new: true }
    );
    if (!updatedCategory)
      return res
        .status(404)
        .send({ message: "Category not found and not updated" });
    return res.send({ updatedCategory });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error updating category" });
  }
};

exports.deleteCategory = async (req, res) => {
  try {
    let categoryId = req.params.id;
    let defaultCategory = await Category.findOne({ name: "Default" });
    if (defaultCategory._id == categoryId)
      return res.send({ message: "Default category cannot be deleted" });
    await Product.updateMany(
      { category: categoryId },
      { category: defaultCategory._id }
    );
    let categoryDeleted = await Category.deleteOne({ _id: categoryId });
    if (!categoryDeleted.deletedCount === 0)
      return res
        .status(404)
        .send({ message: "Category not found and not deleted" });
    return res.send({ message: "Category deleted",categoryDeleted });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error removing category" });
  }
};
