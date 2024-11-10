package seg3x02.employeeGql.resolvers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository

@SpringBootTest
class EmployeesResolverIntegrationTest {

    @Autowired
    private lateinit var employeesResolver: EmployeesResolver

    @Autowired
    private lateinit var employeeRepository: EmployeesRepository

    @BeforeEach
    fun setUp() {
        employeeRepository.deleteAll()
    }

    @Test
    fun `test query employees returns all employees from MongoDB`() {
        // Arrange
        val employee1 = employeeRepository.save(
            Employee(
                name = "Test Name",
                dateOfBirth = "2001-11-11",
                city = "Ottawa",
                salary = 8000f,
                gender = "F",
                email = "test@test.com"
            )
        )

        // Act
        val result = employeesResolver.employees()

        // Assert
        assertEquals(1, result.size)
        assertEquals(employee1.id, result[0].id)
    }

    @Test
    fun `test mutation newEmployee saves to MongoDB`() {
        // Arrange
        val input = CreateEmployeeInput(
            name = "John Doe",
            dateOfBirth = "1950-05-05",
            city = "Ottawa",
            salary = 8000f,
            gender = "F",
            email = "test@test.com"
        )

        // Act
        val result = employeesResolver.newEmployee(input)

        // Assert
        assertNotNull(result.id)
        val savedEmployee = employeeRepository.findById(result.id).orElse(null)
        assertNotNull(savedEmployee)
        assertEquals(input.name, savedEmployee.name)
    }
}