# Software Quality Metrics Report

## 1. Defect Density Calculation

### Definition
Defect Density = Number of Defects / Size (KLOC)
- KLOC = Kilo Lines of Code (1000 lines)

### Calculations by Module

#### NoteService.java
- File: `src/main/java/com/example/notesapp/service/NoteService.java`
- Lines of Code: 85
- Defects Found During Testing: 2
    1. Missing null check in validateNote()
    2. XSS vulnerability in content
- **Defect Density = 2 / 0.085 = 23.5 defects/KLOC**

#### NoteController.java
- File: `src/main/java/com/example/notesapp/controller/NoteController.java`
- Lines of Code: 75
- Defects Found: 1
    1. Missing @Valid annotation on update method
- **Defect Density = 1 / 0.075 = 13.3 defects/KLOC**

#### Note.java
- Lines of Code: 65
- Defects Found: 0
- **Defect Density = 0 / 0.065 = 0 defects/KLOC**

### Overall Project Metrics
- Total Lines of Code: 850
- Total Defects: 5
- **Overall Defect Density = 5 / 0.850 = 5.88 defects/KLOC**

### Industry Comparison
- Excellent: < 1 defect/KLOC
- Good: 1-5 defects/KLOC
- Average: 5-10 defects/KLOC
- Poor: > 10 defects/KLOC

**Our Rating: Average (5.88 defects/KLOC)**

## 2. Mean Time To Failure (MTTF)

### Testing Period Data
- Total Testing Duration: 12 hours
- Total Failures Encountered: 5

### MTTF Calculation
MTTF = Total Operating Time / Number of Failures
MTTF = 12 hours / 5 failures = 2.4 hours

### Breakdown by Test Phase

#### Unit Testing Phase
- Duration: 3 hours
- Failures: 1
- MTTF = 3 hours

#### Integration Testing Phase
- Duration: 4 hours
- Failures: 2
- MTTF = 2 hours

#### UI Testing Phase
- Duration: 5 hours
- Failures: 2
- MTTF = 2.5 hours

## 3. Code Coverage Metrics

Run: `mvn clean test jacoco:report`

### Coverage Results
- **Overall Coverage**: 78%
- **Line Coverage**: 82%
- **Branch Coverage**: 65%
- **Method Coverage**: 90%

### Coverage by Package
- `com.example.notesapp.service`: 95%
- `com.example.notesapp.controller`: 88%
- `com.example.notesapp.model`: 70%
- `com.example.notesapp.repository`: 60%

## 4. Cyclomatic Complexity

### Definition
Measures code complexity based on decision points

### Results from SonarQube
- `NoteService.validateNote()`: 4 (Good)
- `NoteService.sanitizeInput()`: 3 (Good)
- `NoteController.updateNote()`: 5 (Acceptable)

### Scale
- 1-4: Low complexity (Good)
- 5-7: Moderate complexity (Acceptable)
- 8-10: High complexity (Refactor recommended)
- >10: Very high (Must refactor)