"use strict";

const Product = require("../product/product.model");
const Bill = require("./factura.model");
const User = require("../user/user.model")

var cart = [];
exports.addCart = async (req, res) => {
  try {
    let { product, number } = req.body;
    let foundProduct = await Product.findOne({_id: product });

    if (!foundProduct) {
      return res.send({ message: "Product not found" });
    }

    if (!foundProduct.stock) {
      return res.send({ message: "Not available" });
    }

    if (number > foundProduct.stock) {
      return res.send({ message: "There is not more than"+" "+ number + " "+"units in stock" });
    }

    let productos = 0;
    for (let i = 0; i < cart.length; i++) {
      if (cart[i] === product) {
        productos++;
      }
      if (productos * 1 + number * 1 > foundProduct.stock) {
        return res.send({ message: "There is not more than ${`number`} units in stock" });
      }
    }

    for (let i = 0; i < number; i++) {
      cart.push(product);
    }

    let products = await Promise.all(
      cart.map((id) =>
        Product.findOne(
          { _id: id },
          { stock: 0, sales: 0, category: 0, _id: 0 }
        )
      )
    );

    return res.send(products);
  } catch (err) {
    console.error(err);
    return res.status(500).send({
      message: "Error adding product to shopping cart",
      error: err.message,
    });
  }
};

/*exports.cleanCart = async (req, res) => {
  try {
    cart = [];
  } catch (err) {
    return res.status(500).send({ message: "Error cleaning cart" });
  }
};*/

exports.factura = async (req, res) => {
  try {
    let data = req.body;
    data.cliente = req.user.sub;
    let now = new Date();
    data.fecha = now;
    data.products = cart;
    if (data.products.length === 0){  
    return res.send({ message: "You have no products in your cart" });}
      let total = 0;
        let Pprice;
      for (let i = 0; i < data.products.length; i++) {
        Pprice = data.products[i];
        let price = await Product.findOne({ _id: Pprice });
        total = total + price.price;
      }
      data.total = total;
        let rBill = await Bill.create(data);
        let bill = await Bill.findById(rBill._id).populate("products");
        cart.length = 0;
          for (let product of bill.products) {
            let stock = await Product.findById(product.id);
            if (stock) {
              stock.stock--;
              stock.sales++;
              await stock.save();
            }
          }
          let company= "MyMarket Online" 
          let direccion ="5A Avenida 31-61, Zona 3 Cdad. de Guatemala"
          let userId= req.user.sub;
          let user = await User.findOne({_id:userId})
          let message = "Gracias por su Compra" +" "+ user.name
      bill = await Bill.findById(rBill._id).populate("products", [ "name","price", ]);
      return res.status(201).send({company,direccion,bill,message});
    } catch (err) {
      console.error(err);
      return res
        .status(500)
        .send({ message: "Error creating bill"});
    }
  };

  exports.findBills = async (req, res) => {
    try {
      let userId = req.params.id;
      let bills = await Bill.find({ cliente: userId }).populate("products",["name"]);
      if(bills.length===0){
        return res.send({message:"This user has no bills"})
      }
      return res.send(bills);
    } catch (err) {
      console.error(err);
      return res.status(500).send({ message: "Invalid id" });
    }
  };


exports.getProducts=async(req, res)=> {
  try {
    let billId = req.params.id;
    let billExist = await Bill.findById(billId);
    if (!billExist) {
      return res.status(404).send({ message: "Bill not found" });
    }
    let bproducts = await Bill.findById(billId).populate("products");
    return res.send(bproducts.products);
  } catch (err) {
    console.error(err);
    return res.status(500).send({ message: "Error getting products" });
  }
}
