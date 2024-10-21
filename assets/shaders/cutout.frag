#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    float epsilon = 0.0001;
    float isBlack = step(texColor.r, epsilon) * step(texColor.g, epsilon) * step(texColor.b, epsilon);
    gl_FragColor = mix(texColor, vec4(0.0, 0.0, 0.0, 0.0), isBlack);
}
