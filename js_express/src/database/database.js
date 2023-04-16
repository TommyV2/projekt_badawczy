const Sequelize = require('sequelize');

const sequelize = new Sequelize(
    "exampledb",
    "docker",
    "docker",
    {
        host: "db",
        dialect: 'postgres',
    }
);

module.exports = sequelize;