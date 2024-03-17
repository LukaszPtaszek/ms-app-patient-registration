print('### Start initializing MongoDB User ###');

const username = _getEnv('MONGO_DB_USER')
const passwd = _getEnv('MONGO_DB_PASSWD')
const dbName = _getEnv('MONGO_INIT_DATABASE')

db = db.getSiblingDB('admin')

db.createUser({
    user: username,
    pwd: passwd,
    roles: [{role: 'readWrite', db: dbName}]
});

print('### Finished ###');