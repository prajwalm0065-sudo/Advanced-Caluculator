import java.util.Arrays;

/**
 * Matrix.java - Matrix operations module for the Advanced Calculator.
 *
 * Access control : fields and helper methods are private; only the API is public.
 * Static & final : constants are static final, factory/utility methods are static,
 *                  and instances are immutable (every operation returns a new Matrix).
 */
public class Matrix {

    // ---------- Constants (static + final) ----------
    public static final double EPSILON = 1e-10;   // tolerance for "zero" checks
    private static final int PRINT_PRECISION = 4; // decimals used in toString()

    // ---------- State (private + final) ----------
    private final int rows;
    private final int cols;
    private final double[][] data;

    // ---------- Constructors ----------

    /** Creates a rows x cols matrix filled with zeros. */
    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Matrix dimensions must be positive.");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    /** Creates a matrix from a 2D array (deep copy, so outside code can't modify it). */
    public Matrix(double[][] values) {
        if (values == null || values.length == 0 || values[0].length == 0) {
            throw new IllegalArgumentException("Matrix cannot be empty.");
        }
        this.rows = values.length;
        this.cols = values[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (values[i].length != cols) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
            System.arraycopy(values[i], 0, this.data[i], 0, cols);
        }
    }

    // ---------- Static factory methods ----------

    /** Returns an n x n identity matrix. */
    public static Matrix identity(int n) {
        Matrix m = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            m.data[i][i] = 1.0;
        }
        return m;
    }

    /** Returns a rows x cols zero matrix. */
    public static Matrix zero(int rows, int cols) {
        return new Matrix(rows, cols);
    }

    // ---------- Getters ----------

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double get(int row, int col) {
        checkBounds(row, col);
        return data[row][col];
    }

    /** Returns a copy of the underlying data. */
    public double[][] toArray() {
        double[][] copy = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, copy[i], 0, cols);
        }
        return copy;
    }

    public boolean isSquare() {
        return rows == cols;
    }

    // ---------- Core operations ----------

    /** this + other */
    public Matrix add(Matrix other) {
        requireSameSize(other, "addition");
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = this.data[i][j] + other.data[i][j];
            }
        }
        return result;
    }

    /** this - other */
    public Matrix subtract(Matrix other) {
        requireSameSize(other, "subtraction");
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = this.data[i][j] - other.data[i][j];
            }
        }
        return result;
    }

    /** Scalar multiplication: k * this */
    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = this.data[i][j] * scalar;
            }
        }
        return result;
    }

    /** Matrix multiplication: this x other */
    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException(
                "Cannot multiply: columns of first (" + this.cols +
                ") must equal rows of second (" + other.rows + ").");
        }
        Matrix result = new Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                double sum = 0;
                for (int k = 0; k < this.cols; k++) {
                    sum += this.data[i][k] * other.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    /** Transpose of this matrix. */
    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[j][i] = this.data[i][j];
            }
        }
        return result;
    }

    /** Sum of the main diagonal (square matrices only). */
    public double trace() {
        requireSquare("trace");
        double sum = 0;
        for (int i = 0; i < rows; i++) {
            sum += data[i][i];
        }
        return sum;
    }

    /** Determinant via Gaussian elimination with partial pivoting. */
    public double determinant() {
        requireSquare("determinant");
        int n = rows;
        double[][] a = toArray();
        double det = 1.0;

        for (int col = 0; col < n; col++) {
            // find pivot row
            int pivot = col;
            for (int r = col + 1; r < n; r++) {
                if (Math.abs(a[r][col]) > Math.abs(a[pivot][col])) {
                    pivot = r;
                }
            }
            if (Math.abs(a[pivot][col]) < EPSILON) {
                return 0.0;
            }
            if (pivot != col) {
                double[] tmp = a[pivot];
                a[pivot] = a[col];
                a[col] = tmp;
                det = -det; // row swap flips the sign
            }
            det *= a[col][col];
            for (int r = col + 1; r < n; r++) {
                double factor = a[r][col] / a[col][col];
                for (int c = col; c < n; c++) {
                    a[r][c] -= factor * a[col][c];
                }
            }
        }
        return det;
    }

    /** Inverse via Gauss-Jordan elimination. */
    public Matrix inverse() {
        requireSquare("inverse");
        int n = rows;
        double[][] a = toArray();
        double[][] inv = identity(n).data;

        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int r = col + 1; r < n; r++) {
                if (Math.abs(a[r][col]) > Math.abs(a[pivot][col])) {
                    pivot = r;
                }
            }
            if (Math.abs(a[pivot][col]) < EPSILON) {
                throw new ArithmeticException("Matrix is singular and cannot be inverted.");
            }
            swapRows(a, pivot, col);
            swapRows(inv, pivot, col);

            double pivotVal = a[col][col];
            for (int c = 0; c < n; c++) {
                a[col][c] /= pivotVal;
                inv[col][c] /= pivotVal;
            }
            for (int r = 0; r < n; r++) {
                if (r != col) {
                    double factor = a[r][col];
                    for (int c = 0; c < n; c++) {
                        a[r][c] -= factor * a[col][c];
                        inv[r][c] -= factor * inv[col][c];
                    }
                }
            }
        }
        return new Matrix(inv);
    }

    /** this raised to a non-negative integer power (square matrices only). */
    public Matrix power(int exponent) {
        requireSquare("power");
        if (exponent < 0) {
            return inverse().power(-exponent);
        }
        Matrix result = identity(rows);
        Matrix base = this;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = result.multiply(base);
            }
            base = base.multiply(base);
            exponent >>= 1;
        }
        return result;
    }

    // ---------- Object overrides ----------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix)) return false;
        Matrix other = (Matrix) obj;
        if (rows != other.rows || cols != other.cols) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.abs(data[i][j] - other.data[i][j]) > EPSILON) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.deepHashCode(data) + rows * 17 + cols;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String fmt = "%" + (PRINT_PRECISION + 8) + "." + PRINT_PRECISION + "f";
        for (int i = 0; i < rows; i++) {
            sb.append("[");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format(fmt, data[i][j] == 0 ? 0.0 : data[i][j]));
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }

    // ---------- Private helpers (access control) ----------

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index (" + row + ", " + col + ") is out of bounds.");
        }
    }

    private void requireSameSize(Matrix other, String op) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException(
                "Matrices must have the same dimensions for " + op + ".");
        }
    }

    private void requireSquare(String op) {
        if (!isSquare()) {
            throw new IllegalArgumentException("Matrix must be square for " + op + ".");
        }
    }

    private static void swapRows(double[][] m, int r1, int r2) {
        double[] tmp = m[r1];
        m[r1] = m[r2];
        m[r2] = tmp;
    }

    // ---------- Quick test ----------
    public static void main(String[] args) {
        Matrix a = new Matrix(new double[][] {{4, 7}, {2, 6}});
        Matrix b = new Matrix(new double[][] {{1, 2}, {3, 4}});

        System.out.println("A:\n" + a);
        System.out.println("B:\n" + b);
        System.out.println("A + B:\n" + a.add(b));
        System.out.println("A - B:\n" + a.subtract(b));
        System.out.println("A x B:\n" + a.multiply(b));
        System.out.println("2 * A:\n" + a.multiply(2));
        System.out.println("Transpose of A:\n" + a.transpose());
        System.out.println("Determinant of A: " + a.determinant());
        System.out.println("Trace of A: " + a.trace());
        System.out.println("Inverse of A:\n" + a.inverse());
        System.out.println("A x A^-1:\n" + a.multiply(a.inverse()));
        System.out.println("A^3:\n" + a.power(3));
    }
}
