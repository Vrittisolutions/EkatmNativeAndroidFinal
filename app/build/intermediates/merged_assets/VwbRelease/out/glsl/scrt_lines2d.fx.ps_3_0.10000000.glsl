precision mediump float;
uniform sampler2D PenTexture;
varying highp vec4 xlv_COLOR0;
varying highp vec3 xlv_TEXCOORD0;
void main ()
{
  lowp vec4 tmpvar_1;
  tmpvar_1 = texture2D (PenTexture, xlv_TEXCOORD0.xy);
  highp vec4 tmpvar_2;
  tmpvar_2 = (xlv_COLOR0 * tmpvar_1);
  gl_FragData[0] = tmpvar_2;
}

