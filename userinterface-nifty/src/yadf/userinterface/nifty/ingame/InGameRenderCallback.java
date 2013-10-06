package yadf.userinterface.nifty.ingame;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import yadf.userinterface.nifty.IRenderCallback;

public class InGameRenderCallback implements IRenderCallback {

    private int vertexCount;
    private int vaoId;
    private int vboId;

    public void init() {
        // OpenGL expects vertices to be defined counter clockwise by default
        float[] vertices = {
                // Left bottom triangle
                -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f,
                // Right top triangle
                0.5f, -0.5f, 0f, 0.5f, 0.5f, 0f, -0.5f, 0.5f, 0f };
        // Sending data to OpenGL requires the usage of (flipped) byte buffers
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        vertexCount = 6;

        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
    }

    public void deinit() {
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);

        // Delete the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboId);

        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
    }

    @Override
    public void process() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // Bind to the VAO that has all the information about the quad vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);

        // Draw the vertices
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

        // Put everything back to default (deselect)
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
