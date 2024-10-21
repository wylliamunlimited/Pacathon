#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec4 u_colors[10]; // Adjust the size of the array as needed
uniform int u_numColors;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    float colorIndexFloat = texColor.r * float(u_numColors);
    colorIndexFloat = clamp(colorIndexFloat, 0.0, float(u_numColors - 1));
    int colorIndex = int(colorIndexFloat);
    gl_FragColor = u_colors[colorIndex];
}
