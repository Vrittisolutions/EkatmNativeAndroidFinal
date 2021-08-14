uniform highp mat4 matProj;
uniform highp mat4 matWorldView;
uniform highp vec4 BrushMappingMode;
attribute highp vec2 vPosition;
attribute highp vec2 vTexCoord0;
attribute highp vec4 vColor;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec4 xlv_COLOR0;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_2.zw = vec2(0.0, 1.0);
  tmpvar_2.xy = vPosition;
  tmpvar_1 = (tmpvar_2 * (matWorldView * matProj));
  tmpvar_1.y = -(tmpvar_1.y);
  gl_Position = tmpvar_1;
  xlv_TEXCOORD0 = mix (vTexCoord0, (vec2(0.5, 0.5) * (tmpvar_1.xy + vec2(1.0, 1.0))), BrushMappingMode.xx);
  xlv_COLOR0 = vColor;
}

