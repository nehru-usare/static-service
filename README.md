# static-service

## 📌 Overview
`static-service` is a Spring Boot application that allows users to upload static website ZIP files, extract them, and serve them as normal websites.

## 🚀 Features
- **Upload Static Websites**: Users can upload a ZIP file containing their static website.
- **Automatic Extraction**: The ZIP file is extracted to `/app/sites/` under a folder named after the ZIP file.
- **View Uploaded Sites**: The service provides an API to list and access the uploaded websites.
- **Clean UI**: Professional-looking UI built with HTML, CSS, and Thymeleaf.
- **Secure & Scalable**: Built with Java, Spring Boot, and modern security best practices.

## 📂 Project Structure

static-service/ │── src/main/java/com/example/staticservice │ ├── controller/ │ │ ├── SiteController.java │ ├── service/ │ │ ├── SiteService.java │ ├── util/ │ │ ├── FileUtils.java │ ├── StaticServiceApplication.java │── src/main/resources/ │ ├── static/ │ │ ├── css/ │ │ │ ├── styles.css │ │ │ ├── upload.css │ │ │ ├── list.css │ │ ├── js/ │ │ ├── sites/ │ ├── templates/ │ │ ├── list.html │ │ ├── upload.html │ ├── application.properties │── pom.xml │── README.md


## 🛠️ Installation & Setup

### **🔹 Prerequisites**
- Java 1.8+  
- Maven  
- Spring Boot  

### **🔹 Clone the Repository**
```sh
git clone https://github.com/your-username/static-service.git
cd static-service

🔹 Build and Run

mvn clean install
mvn spring-boot:run

🔹 Access the Application

    Upload Page: http://localhost:8080/sites/upload
    List Sites Page: http://localhost:8080/sites/list
    View Site: http://localhost:8080/sites/view/{siteName}

📜 API Endpoints
Method	Endpoint	Description
GET	/sites/list	List all uploaded sites
POST	/sites/upload	Upload a ZIP file
GET	/sites/view/{name}	View a specific site
🎨 UI Screenshots

    (Add screenshots here)

📜 License

This project is licensed under the MIT License.
