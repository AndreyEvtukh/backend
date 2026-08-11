require('dotenv').config();

const API_PORT = process.env.API_PORT || 3000;
const API_HOST = process.env.API_HOST || 'localhost';

module.exports = {
  '/@org/api': {
    target: `http://${API_HOST}:${API_PORT}`,
    secure: false,
  },
};
