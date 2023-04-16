const Sequelize = require('sequelize');
const db = require('../database/database');

const Product = db.define('product', {
  id: {
        type: Sequelize.INTEGER,
        autoIncrement: true,
        allowNull: false,
        primaryKey: true
    },
    product_name: Sequelize.STRING,
    product_price: Sequelize.INTEGER
}, {
  timestamps: false
});

module.exports = Product;