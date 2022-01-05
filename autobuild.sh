cd frontend
echo "Build the frontend..."
npm install
npm run build

cd ..
cp -r frontend/dist/* backend/src/main/resources/static/

cd backend
echo "Build the backend..."
mvn clean install spring-boot:repackage

cd ..
mv backend/target/bookin_back-*-SNAPSHOT.jar ./Bookin.jar
echo "Done !"
