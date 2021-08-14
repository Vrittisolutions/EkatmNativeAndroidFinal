uniform highp mat4 matProj;
uniform highp mat4 matWorldView;
attribute highp vec2 vPosition;
attribute highp vec4 vColor;
varying highp vec4 xlv_COLOR0;
varying highp vec4 xlv_TEXCOORD5;
varying highp vec2 xlv_TEXCOORD0;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  highp vec4 tmpvar_3;
  tmpvar_3.zw = vec2(0.0, 1.0);
  tmpvar_3.xy = vPosition;
  tmpvar_1 = (tmpvar_3 * (matWorldView * matProj));
  tmpvar_2 = tmpvar_1;
  tmpvar_1.y = -(tmpvar_1.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = vColor;
  xlv_TEXCOORD5 = tmpvar_2;
  xlv_TEXCOORD0 = vec2(0.0, 0.0);
}

