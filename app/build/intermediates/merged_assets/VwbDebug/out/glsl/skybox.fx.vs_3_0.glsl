uniform highp mat4 matProj;
uniform highp mat4 matWorldView;
attribute highp vec4 vPosition;
varying highp vec4 xlv_TEXCOORD5;
void main ()
{
  highp vec4 tmpvar_1;
  tmpvar_1.xyz = vPosition.xyz;
  tmpvar_1.w = 1.0;
  gl_Position = (tmpvar_1 * (matWorldView * matProj));
  xlv_TEXCOORD5 = tmpvar_1;
}
