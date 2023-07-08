"use strict";

const userModel = require("./user.model");
const User = require("./user.model");
const { validate } = require("./user.model");
const { encrypt, checkPassword, validateData } = require("../utils/validate");
const { createToken } = require("../services/jwt");
const jwt = require("jsonwebtoken");
const Bill = require("../factura/factura.model");


exports.test = (req, res) => {
  res.send({ message: "Test function is running", user: req.user });
};


exports.register = async (req, res) => {
  try {
    let data = req.body;
    data.password = await encrypt(data.password);
    let adminExist = await User.findOne({role:"ADMIN"})
    if(adminExist){    data.role = "CLIENTE";
  }
    let user = new User(data);
    await user.save();
    return res.status(201).send({ message: "Account created succesfully" });
  } catch (err) {
    console.error(err);
    return res
      .status(500)
      .send({ message: "Error creating account", Error: err.message });
  }
};

exports.getUsers = async (req, res) => {
  try {
    let users = await User.find();
    return res.send({ users });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error getting users" });
  }
};

exports.login = async (req, res) => {
  try {
    let data = req.body;
    let credentials = {
      username: data.username,
      password: data.password,
    };

    let msg = validateData(credentials);
    if (msg) return res.status(400).send({ msg });
    let user = await User.findOne({ username: data.username });
    let userId = user._id;
    let bills = await Bill.find({ cliente: userId });

    if (user && (await checkPassword(data.password, user.password))) {
      let token = await createToken(user);
      return res.send({ message: "User logged succesfully", token,bills });
    }
    return res.send({ message: "Invalid Credentials  " });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error not logged" });
  }
};

exports.updateMyUser = async (req, res) => {
  try {
    let userId = req.user.sub;
    let data = req.body;
    data.password = await encrypt(data.password);
    let role = data.role
   if(role)
      return res.send({message:"You can not update your role"})
      if(!role){
        data.role="CLIENTE"
      }
    let existUser = await User.findOne({ _id: userId });
    if (existUser) {
      if (existUser._id != userId)
        return res.send({ message: "User already created" });
      let updatedUser = await User.findOneAndUpdate({ _id: userId }, data, {
        new: true,
      });
      if (!updatedUser)
        return res
          .status(404)
          .send({ message: "User not found and not updated" });
      return res.send({ updatedUser });
    }
    let updatedUser = await User.findOneAndUpdate({ _id: userId }, data, {
      new: true,
    });
    if (!updatedUser)
      return res
        .status(404)
        .send({ message: "User not found and not updated" });
    return res.send({ updatedUser });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error updating user" });
  }
};
exports.updateUser = async (req, res) => {
  try {
    let userId = req.params.id;
    let data = req.body;
    data.password = await encrypt(data.password);
    let existUser = await User.findOne({ _id:userId });
    if (existUser) {
      if (existUser.role==="ADMIN" ) {
            return res.send({message:"You can not update this User"})
      }   
      if (existUser._id != userId)
        return res.send({ message: "User already created" });
      let updatedUser = await User.findOneAndUpdate({ _id: userId }, data, {
        new: true,
      });
      
      if (!updatedUser)
        return res
          .status(404)
          .send({ message: "User not found and not updated" });
      return res.send({ updatedUser });
    }
    let updatedUser = await User.findOneAndUpdate({ _id: userId }, data, {
      new: true,
    });
    if (!updatedUser)
      return res
        .status(404)
        .send({ message: "User not found and not updated" });
    return res.send({ updatedUser });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error updating user" });
  }
};
exports.deleteUser = async (req, res) => {
  try {
    let userId = req.params.id;
    let deletedUser = await User.findOneAndDelete({ _id: userId });
    if (!deletedUser)
      return res
        .status(404)
        .send({ message: "User not found and not deleted" });
    return res.send({ message: "User deleted sucessfully", deletedUser });
  } catch (err) {
    console.log(err);
    return res.status(500).send({ message: "Error removing User" });
  }
};

exports.deleteProfile = async (req, res) => {
  try {
    let userId = req.user.sub;
    let deletedUser = await User.findOneAndDelete({ _id: userId });
    if (!deletedUser)
      return res
        .status(404)
        .send({ message: "User not found and not deleted" });
    return res.send({ message: "Profile deleted sucessfully", deletedUser });
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Invalid name" });
  }
};