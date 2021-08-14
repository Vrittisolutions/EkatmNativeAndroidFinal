uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
attribute highp vec4 vPosition;
attribute highp vec3 vNormal;
attribute highp vec4 vColor;
attribute highp vec4 vTexCoord1;
attribute highp vec4 vTexCoord2;
attribute highp vec4 vColor1;
attribute highp vec4 vColor2;
varying highp vec4 xlv_COLOR0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp vec4 xlv_COLOR1;
varying highp vec4 xlv_COLOR2;
void main ()
{
  highp vec4 tmpvar_1;
  tmpvar_1.w = vPosition.w;
  tmpvar_1.xyz = (vPosition.xyz * vTexCoord2.xyz);
  tmpvar_1.xyz = (tmpvar_1.xyz + vTexCoord1.xyz);
  tmpvar_1.w = 1.0;
  highp mat3 tmpvar_2;
  tmpvar_2[0] = matWorld[0].xyz;
  tmpvar_2[1] = matWorld[1].xyz;
  tmpvar_2[2] = matWorld[2].xyz;
  gl_Position = (tmpvar_1 * (matWorldView * matProj));
  xlv_COLOR0 = vColor;
  xlv_TEXCOORD1 = (tmpvar_1 * matWorld);
  xlv_TEXCOORD2 = (vNormal * tmpvar_2);
  xlv_COLOR1 = vColor1;
  xlv_COLOR2 = vColor2;
}

