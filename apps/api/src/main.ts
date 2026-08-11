import 'dotenv/config';
import express from 'express';
import { ProductsService } from '@org/api/products';
import {
    ApiResponse,
    Product,
    ProductFilter,
    PaginatedResponse,
} from '@org/models';

const host = process.env.API_HOST ?? 'localhost';
const port = process.env.API_PORT ? Number(process.env.API_PORT) : 3005;
const remote_port = process.env.ROMOTE_PORT ? Number(process.env.ROMOTE_PORT) : 8080;
const SPRING_BOOT_URL = `http://${host}:${remote_port}/graphql`;
const app = express();
const productsService = new ProductsService();

// Middleware
app.use(express.json());

// CORS configuration for Angular app
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header(
        'Access-Control-Allow-Methods',
        'GET, POST, PUT, DELETE, OPTIONS',
    );
    res.header(
        'Access-Control-Allow-Headers',
        'Origin, X-Requested-With, Content-Type, Accept',
    );
    if (req.method === 'OPTIONS') {
        res.sendStatus(200);
    } else {
        next();
    }
});

app.get('/', (req, res) => {
    res.send({message: 'Hello API'});
});

app.post('/graphql', async (req, res) => {
    try {
        const response = await fetch(SPRING_BOOT_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Если нужно прокидывать Authorization:
                ...(req.headers.authorization
                    ? {Authorization: req.headers.authorization}
                    : {}),
            },
            body: JSON.stringify(req.body),
        });

        const data = await response.json();

        // Возвращаем точно такой же статус и тело, что пришло от Spring Boot
        res.status(response.status).json(data);
    } catch (error) {
        console.error('GraphQL proxy error:', error);
        res.status(500).json({errors: [{message: error instanceof Error ? error.message : 'Proxy error'}]});
    }
});

app.listen(port, host, () => {
    console.log(`[ SSR ready ] http://${host}:${port}`);
});
